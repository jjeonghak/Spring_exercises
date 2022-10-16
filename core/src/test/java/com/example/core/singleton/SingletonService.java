package com.example.core.singleton;

public class SingletonService {

    private static final SingletonService instance = new SingletonService();

    public static SingletonService getInstance() {
        return instance;
    }

    //외부에서 새롭게 메모리 할당하는 것을 방지
    private SingletonService() {
    }
}
