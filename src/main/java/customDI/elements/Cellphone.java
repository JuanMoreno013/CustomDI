package customDI.elements;

import customDI.annotations.CustomInject;
import lombok.ToString;

@ToString
public class Cellphone {

    @CustomInject
    private Processor processor;

    @CustomInject
    private Ram doubleRam;

    @CustomInject
    private Memory memory;
}
