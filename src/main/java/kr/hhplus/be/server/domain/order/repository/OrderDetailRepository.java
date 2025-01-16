package kr.hhplus.be.server.domain.order.repository;

import kr.hhplus.be.server.domain.order.entity.OrderDetail;

import java.util.List;

public interface OrderDetailRepository {
    List<OrderDetail> saveAll(List<OrderDetail> orderDetails);
}
