package com.cohetepay.demoui.utils;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.cohetepay.demoui.activities.WMX_Card;
import com.cohetepay.demoui.activities.WMX_Historial_Cancelaciones;
import com.cohetepay.demoui.activities.WMX_Historial_CorteCaja;
import com.cohetepay.demoui.activities.WMX_Menu;
import com.cohetepay.demoui.activities.WMX_Terminal;
import com.cohetepay.demoui.activities.WMX_Transaccion;

import java.util.HashMap;
import java.util.Objects;

public class ActivityFlags {
    private static ActivityFlags instance;

    private HashMap<String,HashMap<FLAGS, Object>> flags;

    public ActivityFlags() {
        this.flags = new HashMap();
        addFlags();
    }

    public static synchronized ActivityFlags getInstance() {
        if(instance == null) {
            instance = new ActivityFlags();
        }
        return instance;
    }

    public HashMap<FLAGS,Object> getByKey(String key) {
        TRACE.d("CURRFLAGS: " + flags + TRACE.NEW_LINE + "KEY: " + key);
        return flags.get(key);
    }

    private void addFlags() {
        flags.put(WMX_Menu.class.getName(), new HashMap<FLAGS, Object>(){{
            put(FLAGS.CHECK_NETWORK, true);
        }});
        flags.put(WMX_Card.class.getName(), new HashMap<FLAGS, Object>(){{
            put(FLAGS.CHECK_NETWORK, true);
        }});
        flags.put(WMX_Transaccion.class.getName(), new HashMap<FLAGS, Object>(){{
            put(FLAGS.CHECK_NETWORK, true);
        }});
        flags.put(WMX_Historial_Cancelaciones.class.getName(), new HashMap<FLAGS, Object>(){{
            put(FLAGS.CHECK_NETWORK, true);
        }});
        flags.put(WMX_Historial_CorteCaja.class.getName(), new HashMap<FLAGS, Object>(){{
            put(FLAGS.CHECK_NETWORK, true);
        }});
    }

}
