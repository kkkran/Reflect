package com.shijie.reflect

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class ShareP {
    companion object{
        private val INSTANCE=ShareP()
        private val TAG:String=ShareP::javaClass.javaClass.simpleName
        private val SHARED_PREFS_LOCK = Any()
        private lateinit var mSharedPrefsName: String
    }

    @Volatile
    private var mWasInitialized:Boolean=false
    @Volatile
    private lateinit var mAppContext: Context

    private lateinit var mData: ConcurrentMap<String, Any>

    private fun initWithContext(context: Context,sharedPrefsName:String): Unit {
        mAppContext=context.applicationContext
        mSharedPrefsName=sharedPrefsName
        val prefs:SharedPreferences=getSharedPreferences()
        mData = ConcurrentHashMap<String,Any>()
        mData.putAll(prefs.all)
        mWasInitialized = true
    }
    private fun getSharedPreferences(): SharedPreferences =mAppContext.getSharedPreferences(mSharedPrefsName,Context.MODE_PRIVATE)

    @Synchronized
    public fun init(context: Context,sharedPrefsName:String): ShareP {
        if (context==null|| TextUtils.isEmpty(sharedPrefsName)){
            throw RuntimeException(
                "You must provide a valid context and shared prefs name when initializing ShareP"
            )
        }
        if (!INSTANCE.mWasInitialized){
            INSTANCE.initWithContext(context,sharedPrefsName)
        }
        return INSTANCE
    }
    private fun getInstance(): ShareP {
        if (!INSTANCE.mWasInitialized) {
            throw java.lang.RuntimeException(
                "ShareP was not initialized! You must call ShareP.init() before using this."
            )
        }
        return INSTANCE
    }
    private fun saveToDisk(key: String, value: Any): Boolean {
        var success:Boolean = false
        synchronized(SHARED_PREFS_LOCK) {

            // 写到磁盘
            val editor = getSharedPreferences().edit()
            var didPut = true
            if (value is Float) {
                editor.putFloat(key, value)
            } else if (value is Int) {
                editor.putInt(key, value)
            } else if (value is Long) {
                editor.putLong(key, value)
            } else if (value is String) {
                editor.putString(key, value)
            } else if (value is Boolean) {
                editor.putBoolean(key, value)
            } else {
                didPut = false
            }
            if (didPut) {
                success = editor.commit()
            }
        }
        return success
    }

    private fun <T:Any> saveAsync(
        key: String,
        value: T,
        callback: Callback?
    ): ShareP? {
        // 先存到内存
        mData[key] = value

        // 再异步存到磁盘
        GlobalScope.launch(Dispatchers.IO) {
            val success=saveToDisk(key,value)
            //主线程回调
            launch(Dispatchers.Main) {
                callback?.apply(success)
            }
        }

        return this
    }
    //清理ShareP缓存
    fun realClear() {
        getInstance().clear(null)
    }
    private fun clear(callback:Callback?) {
        getInstance().mData.clear()
        GlobalScope.launch(Dispatchers.IO) {
            val success = synchronized(SHARED_PREFS_LOCK) {
                val editor = getInstance().getSharedPreferences().edit()
                editor.clear()
                editor.commit()
            }
            launch(Dispatchers.Main) {
                callback?.apply(success)
            }
        }

    }

    /**
     * @param key:String
     */
    //删除ShareP的某个键值对
    fun realRemove(key: String) {
        remove(key, null)
    }
    fun remove(key:String, callback:Callback?){
        getInstance().mData.remove(key)
        GlobalScope.launch(Dispatchers.IO) {
            val success= synchronized(SHARED_PREFS_LOCK){
                val editor= getInstance().getSharedPreferences().edit()
                editor.remove(key)
                editor.commit()
            }
            launch(Dispatchers.Main) {
                callback?.apply(success)
            }
        }
    }
    fun putFloat(key: String, value: Float):ShareP? {
        return getInstance().saveAsync<Float>(key, value, null)
    }
    fun putInt(key: String, value: Int):ShareP? {
        return getInstance().saveAsync<Int>(key, value, null)
    }
    fun putLong(key: String, value: Long):ShareP? {
        return getInstance().saveAsync<Long>(key, value, null)
    }
    fun putString(key: String, value: String):ShareP? {
        return getInstance().saveAsync<String>(key, value, null)
    }
    fun putBoolean(key: String, value: Boolean):ShareP? {
        return getInstance().saveAsync<Boolean>(key, value, null)
    }

    fun getFloat(key: String, fallCallback: Float): Float {
        val value= getInstance().get<Float>(key, Float::class.java)
        return value ?: fallCallback
    }
    fun getInt(key: String, fallCallback: Int): Int {
        val value= getInstance().get<Int>(key, Int::class.java)
        return value ?: fallCallback
    }
    fun getBoolean(key: String, fallCallback: Boolean): Boolean {
        val value= getInstance().get<Boolean>(key, Boolean::class.java)
        return value ?: fallCallback
    }
    fun getString(key: String, fallCallback: String): String {
        val value= getInstance().get<String>(key, String::class.java)
        return value ?: fallCallback
    }
    fun getLong(key: String, fallCallback: Long): Long {
        val value= getInstance().get<Long>(key, Long::class.java)
        return value ?: fallCallback
    }

    private operator fun <T> get(key: String, clazz: Class<T>): T? {
        val value = mData[key]
        var castedObject: T? = null
        if (clazz.isInstance(value)) {
            castedObject = clazz.cast(value)
        }
        return castedObject
    }
    fun containsKey(key: String): Boolean {
        return getInstance().mData.containsKey(key)
    }


    interface Callback {
        //回调函数
        fun apply(success: Boolean?)
    }

}