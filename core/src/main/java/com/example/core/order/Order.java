package com.example.core.order;


    public class Order {
        private Long itemId;
        private String itemName;
        private int itemPrice;
        private int discountPrice;

        public Order(Long itemId, String itemName, int itemPrice, int discountPrice) {
            this.itemId = itemId;
            this.itemName = itemName;
            this.itemPrice = itemPrice;
            this.discountPrice = discountPrice;
        }

        public Long getItemId() {
            return this.itemId;
        }

        public void setItemId(Long itemId) {
            this.itemId = itemId;
        }

        public String getItemName() {
            return this.itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public int getItemPrice() {
            return this.itemPrice;
        }

        public void setItemPrice(int itemPrice) {
            this.itemPrice = itemPrice;
        }

        public int getDiscountPrice() {
            return this.discountPrice;
        }

        public void setDiscountPrice(int discountPrice) {
            this.discountPrice = discountPrice;
        }

        public int calculatePrice() {
            return this.itemPrice - this.discountPrice;
        }

        public String toString() {
            return "Order{itemId=" + this.itemId + ", itemName='" + this.itemName + "', itemPrice=" + this.itemPrice + ", discountPrice=" + this.discountPrice + "}";
        }
    }

