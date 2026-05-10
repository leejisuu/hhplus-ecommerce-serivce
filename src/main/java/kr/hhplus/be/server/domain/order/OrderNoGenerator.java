package kr.hhplus.be.server.domain.order;

import java.time.LocalDate;

public interface OrderNoGenerator {
    String generate(LocalDate date);
}
