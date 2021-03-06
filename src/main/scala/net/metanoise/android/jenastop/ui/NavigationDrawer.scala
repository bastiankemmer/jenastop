/* Copyright 2015 Richard Wiedenhöft <richard@wiedenhoeft.xyz>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.metanoise.android.jenastop.ui

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.util.TypedValue
import android.view.{ Gravity, MenuItem, View, ViewGroup }
import android.widget.{ ArrayAdapter, ListView }

trait NavigationDrawer extends ScalaActivity {

  /**
   * Override this to set the adapter for the navigation list
   */
  protected def navigationAdapter: ArrayAdapter[_]

  /**
   * Override this to set the background color for the navigation drawer
   */
  protected def navigationBackgroundColor: Int = Color.argb(0, 0, 0, 0)

  /**
   * Override this to set the width of the navigation drawer in dp
   */
  protected def navigationWidth: Float = 280

  /**
   * Override this with the string resource that describes the opening of the drawer
   */
  protected def navigationOpenResource: Int

  /**
   * Override this with the string resource that describes the closing of the drawer
   */
  protected def navigationCloseResource: Int

  private lazy val drawerToggle: ActionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, navigationOpenResource, navigationCloseResource)
  private lazy val drawerLayout: DrawerLayout = new DrawerLayout(this)
  private lazy val navigationList: ListView = {
    val list = new ListView(this)
    list.setBackgroundColor(navigationBackgroundColor)
    val params = new DrawerLayout.LayoutParams(
      TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, navigationWidth, getResources.getDisplayMetrics).toInt,
      ViewGroup.LayoutParams.MATCH_PARENT
    )
    params.gravity = Gravity.LEFT
    list.setLayoutParams(params)
    list
  }

  override protected def topContentView: View = {
    drawerLayout.addView(contentView)
    drawerLayout.addView(navigationList)
    navigationList.setAdapter(navigationAdapter)
    drawerLayout
  }

  override protected def onCreate(bundle: Bundle): Unit = {
    super.onCreate(bundle)
    drawerToggle.setDrawerIndicatorEnabled(true)
    val actionBar = getSupportActionBar
    actionBar.setHomeButtonEnabled(true)
    actionBar.setDisplayHomeAsUpEnabled(true)
  }

  override protected def onPostCreate(bundle: Bundle): Unit = {
    super.onPostCreate(bundle)
    drawerToggle.syncState()
  }

  override protected def onConfigurationChanged(config: Configuration): Unit = {
    super.onConfigurationChanged(config)
    drawerToggle.onConfigurationChanged(config)
  }

  override protected def onOptionsItemSelected(item: MenuItem): Boolean = {
    if (drawerToggle.onOptionsItemSelected(item)) true
    else super.onOptionsItemSelected(item)
  }
}
