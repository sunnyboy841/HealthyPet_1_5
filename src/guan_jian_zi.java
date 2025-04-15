public class guan_jian_zi {
     //关键字
    public static void main(String[] args) {
        //常见数据
        System.out.println(666);
        //System.out.println()  输出一行，如果要求输出之后不换行就应当改成System.out.print(888);
        System.out.println(999);
        System.out.println(-888);

        System.out.println("黑马");
        System.out.println("朱门酒肉臭，路有冻死骨");

        System.out.println('好');
        //bool型
        //注意：如果是null则不可以用直接打印，要双引号来打印null字符串
        System.out.println(true);
        System.out.println(false);

        //制表符：\t:在打印的时候，把前面的字符串长度补齐到8或是8的整数倍，最少补一个
        System.out.println("abcd"+'\t');  //直接用加号连接可以了，\t那里单引号双引号都行
        //使用：\t让他更好看
        System.out.println("name"+'\t'+"age");
        System.out.println("tom"+'\t'+"23");
        //输出：

    }
}
