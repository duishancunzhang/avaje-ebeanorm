package com.avaje.tests.query.sqlquery;

import com.avaje.ebean.BaseTestCase;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;
import com.avaje.ebean.plugin.SpiServer;
import com.avaje.tests.model.basic.Customer;
import com.avaje.tests.model.basic.ResetBasicData;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TestNativeILikeExpression extends BaseTestCase {

  @Test
  public void test() {

    boolean expectNative = isExpectNative();

    ResetBasicData.reset();

    Query<Customer> query = Ebean.find(Customer.class)
        .where().ilike("name", "rob")
        .query();

    List<Customer> list = query.findList();

    if (expectNative) {
      assertThat(query.getGeneratedSql()).contains(" from o_customer t0 where t0.name ilike ?");
      assertThat(list).isNotEmpty();
    }
  }

  private boolean isExpectNative() {

    SpiServer pluginApi = server().getPluginApi();
    boolean expressionNativeIlike = pluginApi.getServerConfig().isExpressionNativeIlike();
    String platformName = pluginApi.getDatabasePlatform().getName();

    return expressionNativeIlike && platformName.equalsIgnoreCase("postgres");
  }
}
