/*
 * Copyright (C) 2000 - 2018 Silverpeas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have received a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "https://www.silverpeas.org/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.silverpeas.core.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.silverpeas.core.admin.PaginationPage;
import org.silverpeas.core.test.UnitTest;
import org.silverpeas.core.util.SilverpeasArrayList;
import org.silverpeas.core.util.SilverpeasList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

/**
 * @author silveryocha
 */
@UnitTest
class PaginationTest {

  private List<Integer> dataSource = new ArrayList<>();
  private Function<PaginationPage, SilverpeasList<Integer>> simplePaginatedDataSource = p -> {
    int start = (p.getPageNumber() - 1) * p.getPageSize();
    if (start >= this.dataSource.size()) {
      return new SilverpeasArrayList<>(0);
    }
    int end = start + p.getPageSize();
    if (end > this.dataSource.size()) {
      end = this.dataSource.size();
    }
    return new SilverpeasArrayList<>(this.dataSource.subList(start, end));
  };

  @BeforeEach
  void setup() {
    for (int i = 0; i < 1000; i++) {
      dataSource.add(i);
    }
  }

  @Test
  void firstPageWithoutFilteringShouldWork() {
    final List<Integer> result =
        new Pagination<Integer, SilverpeasList<Integer>>(new PaginationPage(1, 5))
        .paginatedDataSource(simplePaginatedDataSource)
        .filter(r -> r)
        .factor(1)
        .execute();
    assertThat(result.size(), is(5));
    assertThat(result, contains(0, 1 ,2 ,3 ,4));
  }

  @Test
  void secondPageWithoutFilteringShouldWork() {
    final List<Integer> result =
        new Pagination<Integer, SilverpeasList<Integer>>(new PaginationPage(2, 3))
        .paginatedDataSource(simplePaginatedDataSource)
        .filter(r -> r)
        .factor(1)
        .execute();
    assertThat(result.size(), is(3));
    assertThat(result, contains(3 ,4, 5));
  }

  @Test
  void firstPageButFilteringAlmostAllDataShouldWork() {
    final List<Integer> result =
        new Pagination<Integer, SilverpeasList<Integer>>(new PaginationPage(1, 5))
        .paginatedDataSource(simplePaginatedDataSource)
        .filter(r -> r.stream().filter(i -> i > 996).collect(SilverpeasList.collector(r)))
        .factor(1)
        .execute();
    assertThat(result.size(), is(3));
    assertThat(result, contains(997, 998, 999));
  }

  @Test
  void middlePageWithFilteringAlmostAllDataShouldWork() {
    final List<Integer> result =
        new Pagination<Integer, SilverpeasList<Integer>>(new PaginationPage(100, 5))
        .paginatedDataSource(simplePaginatedDataSource)
        .filter(r -> r.stream().filter(i -> i > 996).collect(SilverpeasList.collector(r)))
        .factor(1)
        .execute();
    assertThat(result.size(), is(3));
    assertThat(result, contains(997, 998, 999));
  }

  @Test
  void middlePageWithFilteringShouldWork() {
    final List<Integer> result =
        new Pagination<Integer, SilverpeasList<Integer>>(new PaginationPage(100, 5))
        .paginatedDataSource(simplePaginatedDataSource)
        .filter(r -> r.stream().filter(i -> i > 496 && i != 499).collect(SilverpeasList.collector(r)))
        .factor(1)
        .execute();
    assertThat(result.size(), is(5));
    assertThat(result, contains(497, 498, 500, 501, 502));
  }

  @Test
  void justLittleDataShouldWork() {
    final List<Integer> result =
        new Pagination<Integer, SilverpeasList<Integer>>(new PaginationPage(1, 5))
        .paginatedDataSource(p -> new SilverpeasArrayList<>(Arrays.asList(3, 7, 8)))
        .filter(r -> r)
        .execute();
    assertThat(result.size(), is(3));
    assertThat(result, contains(3, 7, 8));
  }
}