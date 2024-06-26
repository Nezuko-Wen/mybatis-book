/**
 *    Copyright 2009-2024 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.submitted.quotedcolumnnames;

import java.io.Reader;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.BaseDataTest;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class QuotedColumnNamesTest {

  protected static SqlSessionFactory sqlSessionFactory;

  @BeforeClass
  public static void setUp() throws Exception {
    try (Reader reader = Resources.getResourceAsReader("org/apache/ibatis/submitted/quotedcolumnnames/MapperConfig.xml")) {
      sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
    }

    BaseDataTest.runScript(sqlSessionFactory.getConfiguration().getEnvironment().getDataSource(),
            "org/apache/ibatis/submitted/quotedcolumnnames/CreateDB.sql");
  }

  @Test
  public void testIt() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      List<Map<String, Object>> list = sqlSession.selectList("org.apache.ibatis.submitted.quotedcolumnnames.Map.doSelect");
      printList(list);
      assertColumnNames(list);
    }
  }

  @Test
  public void testItWithResultMap() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      List<Map<String, Object>> list = sqlSession.selectList("org.apache.ibatis.submitted.quotedcolumnnames.Map.doSelectWithResultMap");
      printList(list);
      assertColumnNames(list);
    }
  }

  private void assertColumnNames(List<Map<String, Object>> list) {
    Map<String, Object> record = list.get(0);

    Assert.assertTrue(record.containsKey("firstName"));
    Assert.assertTrue(record.containsKey("lastName"));

    Assert.assertFalse(record.containsKey("FIRST_NAME"));
    Assert.assertFalse(record.containsKey("LAST_NAME"));
  }

  private void printList(List<Map<String, Object>> list) {
    for (Map<String, Object> map : list) {
      Assert.assertNotNull(map);
    }
  }
}
