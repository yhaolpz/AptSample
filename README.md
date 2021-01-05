# Apt Sample

基于 APT、JavaPoet，自动生成 Activity/Fragment 对应的 ViewModel 类。

## APT

APT 即 Annotation Processing Tool，编译时注解处理器，是 javac 的一个工具，用于编译时扫描和处理注解。

通过 APT 可以获取到注解和被注解对象的相关信息，在拿到这些信息后我们可以根据需求自动生成一些代码，省去了手动编写。

获取注解及生成代码在代码编译期完成，相比反射在运行时处理注解大大提高了程序性能。

## JavaPoet

JavaPoet 是一个辅助生成 Java 源代码的工具库，见 https://github.com/square/javapoet 。

APT 结合 JavaPoet，可以便捷的生成代码。