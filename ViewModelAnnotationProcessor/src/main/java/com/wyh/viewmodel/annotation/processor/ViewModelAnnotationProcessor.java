package com.wyh.viewmodel.annotation.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.wyh.viewmodel.annotation.ViewModelAutoGen;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

/**
 * 处理 {@link ViewModelAutoGen} 注解，自动生成对应的 ViewModel 类
 */
@AutoService(Processor.class)
public class ViewModelAnnotationProcessor extends AbstractProcessor {

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(ViewModelAutoGen.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment env) {
        if (env.processingOver()) {
            return true;
        }
        for (Element element : env.getElementsAnnotatedWith(ViewModelAutoGen.class)) {
            if (element.getKind() != ElementKind.CLASS) {
                error(String.format("Only classes can be annotated with @%s", ViewModelAutoGen.class.getSimpleName()));
                continue;
            }
            final String className = element.toString();
            final String simpleClassName = element.getSimpleName().toString();
            final String packageName = className.substring(0, className.lastIndexOf("."));
            String annotationValue = "";
            Element actionElement = processingEnv.getElementUtils().getTypeElement(ViewModelAutoGen.class.getName());
            TypeMirror actionType = actionElement.asType();
            for (AnnotationMirror am : element.getAnnotationMirrors()) {
                if (am.getAnnotationType().equals(actionType)) {
                    for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : am.getElementValues().entrySet()) {
                        if ("value".equals(entry.getKey().getSimpleName().toString())) {
                            annotationValue = entry.getValue().getValue().toString();
                            break;
                        }
                    }
                }
            }
            if (className.length() > 0 && annotationValue.length() > 0) {
                genViewModelJavaFile(packageName, className, simpleClassName, annotationValue);
            }
        }
        return true;
    }


    /**
     * 生成对应的 ViewModel 类
     *
     * @param packageName     包名
     * @param className       类名
     * @param simpleClassName 类名
     * @param annotationValue 注解值
     */
    private void genViewModelJavaFile(final String packageName,
                                      final String className,
                                      final String simpleClassName,
                                      final String annotationValue) {
        note("genViewModelJavaFile className=" + className
                + ", packageName=" + packageName
                + "，simpleClassName=" + simpleClassName
                + "，annotationValue=" + annotationValue);
        final String[] annotationClassNames = annotationValue.split(",");
        final String[] annotationSimpleNames = new String[annotationClassNames.length];
        for (int i = 0; i < annotationClassNames.length; i++) {
            annotationClassNames[i] = annotationClassNames[i].substring(0, annotationClassNames[i].lastIndexOf("."));
            annotationSimpleNames[i] = annotationClassNames[i].substring(annotationClassNames[i].lastIndexOf(".") + 1);
        }
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(simpleClassName + "ViewModel")
                .superclass(ClassName.get("androidx.lifecycle", "ViewModel"))
                .addModifiers(Modifier.PUBLIC);
        for (int i = 0; i < annotationClassNames.length; i++) {
            String annotationPackageName = annotationClassNames[i].substring(0, annotationClassNames[i].lastIndexOf("."));
            ClassName annotationClass = ClassName.get(annotationPackageName, annotationSimpleNames[i]);
            //生成成员变量
            classBuilder.addField(FieldSpec.builder(annotationClass, "m" + annotationSimpleNames[i], Modifier.PRIVATE)
                    .initializer("new $T()", annotationClass)
                    .build());
            //生成方法
            classBuilder.addMethod(MethodSpec.methodBuilder("get" + annotationSimpleNames[i])
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("return $N", "m" + annotationSimpleNames[i])
                    .returns(annotationClass)
                    .build());
        }
        JavaFile javaFile = JavaFile.builder(packageName, classBuilder.build()).build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void note(String msg) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, msg);
    }

    private void error(String msg) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg);
    }

}