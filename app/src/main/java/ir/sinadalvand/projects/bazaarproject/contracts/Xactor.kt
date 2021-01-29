package ir.sinadalvand.projects.bazaarproject.contracts

import android.content.Context


/**
 * simple class for manage read and write in SharePreferences
 * try to extend and make singleton object for whole application
 * @constructor
 * @param context Context application context
 * @param prefname String? string for make preference file with this name !
 */
abstract class Xactor(context: Context, prefname: String) {

    private val securePref = context.getSharedPreferences(prefname, Context.MODE_PRIVATE)
    private val editor = securePref.edit()


    protected fun get(key: String, default: String?): String? {
        return securePref.getString(key, default)
    }


    protected fun get(key: String, default: Boolean): Boolean {
        return securePref.getBoolean(key, default)
    }


    protected fun get(key: String, default: Int): Int {
        return securePref.getInt(key, default)
    }


    protected fun get(key: String, default: Long): Long {
        return securePref.getLong(key, default)
    }


    protected fun get(key: String, default: Float): Float {
        return securePref.getFloat(key, default)
    }


    protected fun put(key: String, value: String?) {
        editor.putString(key, value)
        apply()
    }


    protected fun put(key: String, value: Boolean) {
        editor.putBoolean(key, value)
        apply()
    }


    protected fun put(key: String, value: Int) {
        editor.putInt(key, value)
        apply()
    }

    protected fun put(key: String, value: Long) {
        editor.putLong(key, value)
        apply()
    }


    protected fun put(key: String, value: Float) {
        editor.putFloat(key, value)
        apply()
    }


    private fun apply() {
        editor.apply()
    }


    protected fun remove(key: String, useless: String = "") {
        editor.remove(key)
        editor.commit()
    }


}