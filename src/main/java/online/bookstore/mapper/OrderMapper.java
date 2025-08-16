package online.bookstore.mapper;

import online.bookstore.config.MapperConfig;
import online.bookstore.dto.order.CreateOrderRequestDto;
import online.bookstore.dto.order.OrderDto;
import online.bookstore.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "orderItems", target = "items")
    OrderDto toDto(Order order);

    Order toModel(CreateOrderRequestDto requestDto);
}
