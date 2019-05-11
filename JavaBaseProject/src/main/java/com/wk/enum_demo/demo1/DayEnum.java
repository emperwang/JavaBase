package com.wk.enum_demo.demo1;

public enum DayEnum {
    MONDAY,TUESDAY,WEDNESDAY,THURSDAY,
    FRIDAY,SATURDAY,SUNDAY
}

/**  编译后的结果
 * public final class DayEnum extends Enum<DayEnum> {
 *   //编译器为我们添加的静态的values()方法
 *   public static DayEnum[] values() {
 *   return (DayEnum[])$VALUES.clone(); } /
 *   /编译器为我们添加的静态的valueOf()方法，注意间接调用了Enum也类的valueOf方法
 *   public static DayEnum valueOf(String s) {
 *   return (DayEnum)Enum.valueOf(com/fsx/run/enums/DayEnum, s); }
 *   //私有构造函数 只能由编译器来调用
 *   private DayEnum(String enumName, int index) {
 *   super(enumName, index);
 *   }
 *   //前面定义的7种枚举实例
 *   public static final DayEnum MONDAY;
 *   public static final DayEnum TUESDAY;
 *   public static final DayEnum WEDNESDAY;
 *   public static final DayEnum THURSDAY;
 *   public static final DayEnum FRIDAY;
 *   public static final DayEnum SATURDAY;
 *   public static final DayEnum SUNDAY;
 *
 *   //装载所有实例的一个数组
 *   private static final DayEnum $VALUES[];
 *   //通过静态代码快实例这些多例
 *   static { MONDAY = new DayEnum("MONDAY", 0);
 *   TUESDAY = new DayEnum("TUESDAY", 1);
 *   WEDNESDAY = new DayEnum("WEDNESDAY", 2);
 *   THURSDAY = new DayEnum("THURSDAY", 3);
 *   FRIDAY = new DayEnum("FRIDAY", 4);
 *   SATURDAY = new DayEnum("SATURDAY", 5);
 *   SUNDAY = new DayEnum("SUNDAY", 6);
 *
 *   //都装载进去
 *   $VALUES = (new DayEnum[] {
 *   MONDAY, TUESDAY, WEDNESDAY,
 *   THURSDAY, FRIDAY, SATURDAY, SUNDAY });
 *   }
 *   }
 */