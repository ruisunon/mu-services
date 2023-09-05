package com.redis.om.spring.search.stream.predicates.numeric;

import java.time.*;
import java.util.Date;

import com.redis.om.spring.metamodel.SearchFieldAccessor;
import com.redis.om.spring.search.stream.predicates.BaseAbstractPredicate;

import redis.clients.jedis.search.querybuilder.Node;
import redis.clients.jedis.search.querybuilder.QueryBuilders;
import redis.clients.jedis.search.querybuilder.Values;

public class NotEqualPredicate<E, T> extends BaseAbstractPredicate<E, T> {
  private final T value;

  public NotEqualPredicate(SearchFieldAccessor field, T value) {
    super(field);
    this.value = value;
  }

  public T getValue() {
    return value;
  }

  @Override
  public Node apply(Node root) {
    Class<?> cls = value.getClass();
    if (cls == LocalDate.class) {
      LocalDate localDate = (LocalDate) getValue();
      Instant instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
      long unixTime = instant.getEpochSecond();

      return QueryBuilders.intersect(root)
          .add(QueryBuilders.disjunct(getSearchAlias(), Values.eq(Integer.parseInt(Long.toString(unixTime)))));
    } else if (cls == Date.class) {
      Date date = (Date) getValue();
      Instant instant = date.toInstant();
      long unixTime = instant.getEpochSecond();
      return QueryBuilders.intersect(root)
          .add(QueryBuilders.disjunct(getSearchAlias(), Values.eq(Integer.parseInt(Long.toString(unixTime)))));
    } else if (cls == LocalDateTime.class) {
      LocalDateTime localDateTime = (LocalDateTime) getValue();
      Instant instant = localDateTime.toInstant(ZoneOffset.of(ZoneId.systemDefault().getId()));
      long unixTime = instant.getEpochSecond();
      return QueryBuilders.intersect(root)
          .add(QueryBuilders.disjunct(getSearchAlias(), Values.eq(Integer.parseInt(Long.toString(unixTime)))));
    } else if (cls == Instant.class) {
      Instant instant = (Instant) getValue();
      long unixTime = instant.getEpochSecond();
      return QueryBuilders.intersect(root)
          .add(QueryBuilders.disjunct(getSearchAlias(), Values.eq(Integer.parseInt(Long.toString(unixTime)))));
    } else if (cls == Integer.class) {
      return QueryBuilders.intersect(root)
          .add(QueryBuilders.disjunct(getSearchAlias(), Values.eq(Integer.parseInt(getValue().toString()))));
    } else if (cls == Double.class) {
      return QueryBuilders.intersect(root)
          .add(QueryBuilders.disjunct(getSearchAlias(), Values.eq(Double.parseDouble(getValue().toString()))));
    } else {
      return root;
    }
  }

}
