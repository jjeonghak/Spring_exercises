package jpabook.jpashop.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class MemberProduct {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member pmMember;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product pmProduct;

    private LocalDateTime orderDate;
    private int orderAmount;
}
