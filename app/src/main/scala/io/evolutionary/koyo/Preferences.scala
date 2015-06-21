package io.evolutionary.koyo

import android.content.{SharedPreferences, Context}
import android.preference.PreferenceManager

object Preferences {

  private def getPrefs(context: Context): SharedPreferences =
    PreferenceManager.getDefaultSharedPreferences(context)

  def putString(key: String, value: String)(implicit context: Context): Unit =
    getPrefs(context).edit().putString(key, value).commit()

  def getString(key: String, defValue: String)(implicit context: Context): String =
    getPrefs(context).getString(key, defValue)

}