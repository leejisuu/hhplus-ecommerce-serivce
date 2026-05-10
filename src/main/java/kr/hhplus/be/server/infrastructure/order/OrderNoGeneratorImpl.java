package kr.hhplus.be.server.infrastructure.order;

import kr.hhplus.be.server.domain.order.OrderNoGenerator;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class OrderNoGeneratorImpl implements OrderNoGenerator {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final AtomicLong sequence = new AtomicLong();

    @Override
    public String generate(LocalDate currentDate) {
        String date = currentDate.format(FORMATTER);
        long orderSequence = sequence.incrementAndGet();
        return date + "-" + orderSequence;
    }
}
