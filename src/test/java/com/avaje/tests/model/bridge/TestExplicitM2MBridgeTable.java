package com.avaje.tests.model.bridge;

import com.avaje.ebean.BaseTestCase;
import com.avaje.ebean.Ebean;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TestExplicitM2MBridgeTable extends BaseTestCase {

  @Test
  public void test() {

    BUser user = new BUser("Rob");
    BSite site = new BSite("avaje.org");

    Ebean.save(user);
    Ebean.save(site);

    insertUpdateBridge(user, site);
    insertUpdateBridgeB(user, site);
    insertUpdateBridgeC(user, site);
  }

  /**
   * Test where matching by db column naming convention.
   */
  private void insertUpdateBridge(BUser user, BSite site) {

    BSiteUser access = new BSiteUser(BAccessLevel.ONE, site, user);
    Ebean.save(access);

    access.setAccessLevel(BAccessLevel.TWO);
    Ebean.save(access);

    List<BSiteUser> list = Ebean.find(BSiteUser.class).findList();
    assertThat(list).isNotEmpty();

    for (BSiteUser bridge : list) {
      assertThat(bridge.getId().siteId).isEqualTo(bridge.getSite().id);
      assertThat(bridge.getId().userId).isEqualTo(bridge.getUser().id);
    }
  }

  /**
   * Test where matching by property name.
   */
  private void insertUpdateBridgeB(BUser user, BSite site) {

    BSiteUserB access = new BSiteUserB(BAccessLevel.ONE, site, user);
    Ebean.save(access);

    access.setAccessLevel(BAccessLevel.TWO);
    Ebean.save(access);

    List<BSiteUserB> list = Ebean.find(BSiteUserB.class).findList();
    assertThat(list).isNotEmpty();

    for (BSiteUserB bridge : list) {
      assertThat(bridge.getId().site).isEqualTo(bridge.getSite().id);
      assertThat(bridge.getId().user).isEqualTo(bridge.getUser().id);
    }
  }

  /**
   * Test where matching by explicit @JoinColumn.
   */
  private void insertUpdateBridgeC(BUser user, BSite site) {

    BSiteUserC access = new BSiteUserC(BAccessLevel.ONE, site, user);
    Ebean.save(access);

    access.setAccessLevel(BAccessLevel.TWO);
    Ebean.save(access);

    List<BSiteUserC> list = Ebean.find(BSiteUserC.class).findList();
    assertThat(list).isNotEmpty();

    for (BSiteUserC bridge : list) {
      assertThat(bridge.getId().siteUid).isEqualTo(bridge.getSite().id);
      assertThat(bridge.getId().userUid).isEqualTo(bridge.getUser().id);
    }
  }
}
