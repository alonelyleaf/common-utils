package com.alonelyleaf.designpattern.structuremode.decorator;

import com.alonelyleaf.designpattern.structuremode.decorator.interceptor.HandlerInterceptor;

/**
 *
 * 装饰器主要解决的是直接继承下因功能的不断横向扩展导致⼦类膨胀的问题，⽽是用装饰器模式后就会比直接继承显得更加灵活同时这样也就不再需要考虑子类的维护。
 * 在装饰器模式中有四个⽐较要点抽象出来的点;
 * 1. 抽象构件⻆色(Component) - 定义抽象接口
 * 2. 具体构件⻆色(ConcreteComponent) - 实现抽象接口，可以是一组
 * 3. 装饰⻆色(Decorator) - 定义抽象类并继承接⼝中的方法，保证一致性
 * 4. 具体装饰⻆色(ConcreteDecorator) - 扩展装饰具体的实现逻辑
 *
 * 一般在业务开发的初期，往往内部的ERP使⽤只需要判断账户验证即可，验证通过后即可访问ERP的所 有资源。但随着业务的不不断发展，团队⾥里里开始出现专门
 * 的运营⼈员、营销人员、数据人员，每个⼈员对 于ERP的使⽤需求不不同，有些需要创建活动，有些只是查看数据。同时为了保证数据的安全性，不会让每个用户都有最高的权限。
 *
 * 那么以往使⽤的 SSO 是⼀一个组件化通用的服务，不能在⾥面添加需要的⽤户访问验证功能。这个时候我 们就可以使⽤装饰器模式，扩充原有的单点登录服务。
 * 但同时也保证原有功能不受破坏，可以继续使⽤。
 *
 * 还有一种场景也可以使⽤装饰器。例如;你之前使用某个实现某个接口接收单个消息，但由 于外部的升级变为发送 list 集合消息，但你⼜不希望所有的代码类都
 * 去修改这部分逻辑。那么可以使用装饰器模式进⾏适配 list 集合，给使⽤者依然是 for 循环后的单个消息。
 *
 * 在装饰类中有两个点的地方是;1)继承了处理接口、2)提供了构造函数、3)覆盖了方法 preHandle 。
 * 以上三个点是装饰器模式的核心处理部分，这样可以踢掉对⼦类继承的⽅方式实现逻辑功能扩展。
 */
public abstract class SsoDecorator implements HandlerInterceptor {

    private HandlerInterceptor handlerInterceptor;

    private SsoDecorator(){}

    public SsoDecorator(HandlerInterceptor handlerInterceptor) {
        this.handlerInterceptor = handlerInterceptor;
    }

    public boolean preHandle(String request, String response, Object handler) {
        return handlerInterceptor.preHandle(request, response, handler);
    }

}
