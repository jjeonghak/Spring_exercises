package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    private Item createItem(String name, int price, int stockQuantity) {
        Item book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember(String name) {
        Member member = new Member();
        member.setName(name);
        member.setAddress(new Address("seoul", "street1", "123-123"));
        em.persist(member);
        return member;
    }

    @Test
    public void 상품주문() throws Exception {
        //given
        Member member = createMember("member1");
        Item book = createItem("book1", 10000, 10);

        //when
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        Assert.assertEquals("상품 주문 상태", OrderStatus.ORDER, getOrder.getStatus());
        Assert.assertEquals("주문한 상품 종류의 수", 1, getOrder.getOrderItems().size());
        Assert.assertEquals("주문한 상품 가격", book.getPrice() * orderCount, getOrder.getTotalPrice());
        Assert.assertEquals("주문한 상품 재고", 10 - 2, book.getStockQuantity());

    }

    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception {
        //given
        Member member = createMember("member1");
        Item book = createItem("book1", 10000, 10);
        int orderCount = 11;

        //when
        orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Assert.fail("NotEnoughStockException 발생안함");
    }

    @Test
    public void 주문취소() throws Exception {
        //given
        Member member = createMember("member1");
        Item book = createItem("book1", 10000, 10);
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);
        Assert.assertEquals("주문 취소 상태", OrderStatus.CANCEL, getOrder.getStatus());
        Assert.assertEquals("상품 재고", 10, book.getStockQuantity());
    }

}