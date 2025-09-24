package br.com.dio.model;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@ToString
@Getter
public class Money {

    private final List<MoneyAudit> history = new ArrayList<>();

    public Money(MoneyAudit history){
        this.history.add(history);
    }

    public void addHistory(MoneyAudit history){
        this.history.add(history);
    }





}
