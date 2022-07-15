package customDI.elements;

import customDI.annotations.CustomInject;
import lombok.ToString;

@ToString
public class Computer {

    @CustomInject
    private Processor intelProcessor;

    @CustomInject
    private Ram doubleRam64;

    @CustomInject
    private Memory memory;
}
