package com.dragonguard.backend.config.batch;

import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class NoOpWriter<T> implements ItemWriter<T> {

    @Override
    public void write(List<? extends T> items) throws Exception {}
}
