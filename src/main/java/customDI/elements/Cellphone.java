package customDI.elements;

import customDI.annotations.CustomInject;
import lombok.ToString;

@ToString
public class Cellphone {

    @CustomInject
    private Processor intelProcessor;

    @CustomInject
    private Ram ram;

    @CustomInject
    private Memory memory;
}
