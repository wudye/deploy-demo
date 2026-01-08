第一个定义，最正宗的定义：If for each object o1 of type S there is an object o2 of type T
such that for all programs P defined in terms of T, the behavior of P is unchanged when o1 is
substituted for o2 then S is a subtype of T.
如果对每一个类型为 T1 的对象 o1，都有类型为 T2 的对象 o2，使得以 T1 定义的所有程序 P 在所有的
对象 o1 都代换成 o2 时，程序 P 的行为没有发生变化，那么类型 T2 是类型 T1 的子类型。
第二个定义，functions that use pointers or references to base classes must be able to use
objects of derived classes without knowing it.
所有引用基类的地方必须能透明地使用其子类的对象。
第二个定义是最清晰明确的，通俗点讲只要父类能出现的地方我子类就可以出现，而且调用子类还不
产生任何的错误或异常，调用者可能根本就不需要知道是父类还是子类。但是反过来就不成了，有子类出
现的地方，父类未必就能适应，里氏替换法则包含了四层意思：
子类必须完全的实现父类的方法。我们在做系统设计时，经常会定义一个接口或者抽象类，然后编写
实现，调用类则直接传入接口或抽象类，其实这里已经使用了里氏替换法则。