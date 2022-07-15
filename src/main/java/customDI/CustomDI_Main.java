package customDI;

public class CustomDI_Main {

    public static void main(String[] args) {
        CustomDIImpl customDI = new CustomDIImpl();

        System.out.println("Beans");
        customDI.getAllBeans().forEach((name, bean) -> System.out.println(bean));

        System.out.println("Dependents");
        customDI.getBeanDependents().forEach((name, bean) -> System.out.println(bean));

    }
}
