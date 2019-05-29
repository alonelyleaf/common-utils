
# dispatcher 包

内容分发，对一个数据源的数据可能区分不同的执行方式；本质还是方法调用，只是判断和调用的方式不同

**实现方式：**
1. 通过实现`BeanPostProcessor`接口,对bean进行后置处理，对于标记了`@DispatchClass`类中的`@DispatchMethod` 的方法进行记录，将其class、method及parameterTypes封装起来并存放到map中
2. 收到消息后根据type找到对应的方法Method，然后通过invoke执行该方法



