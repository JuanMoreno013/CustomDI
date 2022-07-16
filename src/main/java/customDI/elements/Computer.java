package customDI.elements;

import customDI.annotations.CustomInject;
import lombok.ToString;

@ToString
public class Computer {

    @CustomInject
    private Processor processor;

    @CustomInject
    private Ram doubleRam;

    @CustomInject
    private Memory memory;
}
