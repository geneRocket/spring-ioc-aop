模仿精简spring框架的代码，保留核心功能。spring源码非常多，很多复杂但不是核心的功能。

# 支持特性

## ioc

- [x] set注入
- [x] 循环依赖
- [x] 自动名字注入
- [ ] 构造函数注入
- [ ] 自动类型注入

## aop

- [x] 注解配置切点
- [x] before
- [x] after
- [x] JdkProxy
- [x] CglibProxy

# ioc源码解析

之前在博客写的两篇文章

https://blog.csdn.net/qq789045/article/details/105934947

https://blog.csdn.net/qq789045/article/details/108700449

# aop结构分析

## 入口

xml中通过aspectj-autoproxy开启aop功能，相当于在容器中注册AnnotationAwareAspectJAutoProxyCreator这个类。实际上实在ApplicationContext在启动refresh的时候会找出已注册的beandefinition，看看是否实现了
BeanPostProcessor，如果是，则会创建bean。以后新建的bean，都会通过在init阶段经过BeanPostProcessor处理。
aop功能就是在bean init后，创建代理对象。

## 注解

带有注解@Aspect是配置切面的类，需要在容器中注册，使用xml配置。

## advisor

advisor是一个记录了执行时候、pointcut、执行哪个函数、哪个对象。。同时可返回advice。

### 建立所有advisor

首先遍历容器里所有bean，找出带有@Aspect注解的类，然后找出before、after之类的。对每个切面建立advisor，
记录执行时候、pointcut、执行哪个函数、哪个对象。

### 匹配合适advisor

遍历所有advisor、遍历被代理类的所有方法，找出是否至少有一个方法需要代理。有的话则这个advisor合适。

## advice

advice是执行控制执行时机的，可以在被代理函数之前，也可以在之后。

## 代理链

一条advice链表表示要代理的方法，遍历所有advice，然后被代理的函数执行一次。
after的话，先不执行切点函数，继续遍历下一个advice，然后通过递归返回，再执行。
