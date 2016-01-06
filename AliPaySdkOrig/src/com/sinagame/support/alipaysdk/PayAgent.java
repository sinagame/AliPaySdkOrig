package com.sinagame.support.alipaysdk;

public class PayAgent {

    public static interface PayStateListener {
        void onSuccess();

        void onFailure();

        void onRunning();

        void onCancler();

        void onOffLine();

        void onCheckNO();
    }

    PayStateListener m;

    public PayAgent(String[] s, PayStateListener l) {
        super();
        a = s;
        m = l;
    }

    protected boolean b(String i) {
        return true;
    }

    public void pay(final android.app.Activity act, String arg1, String arg2,
                    String arg3, String arg4, String arg5) {
        if (m == null || a == null || a.length >> 2 != 1) {
            return;
        }
        String o = g(arg1, arg2, arg3, arg4, arg5);
        String s = s(o, a[3]);
        try {
            s = java.net.URLEncoder.encode(s, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String i = o + "&sign=\"" + s + "\"&" + "sign_type=\"RSA\"";
        new Thread(new Runnable() {
            @Override
            public void run() {
                com.alipay.sdk.app.PayTask p = new com.alipay.sdk.app.PayTask(
                        act);
                String r = p.pay(i);
                PayResult payResult = new PayResult(r);
                String f = payResult.getResult();
                if (!b(f)) {
                    new android.os.Handler(android.os.Looper.getMainLooper())
                            .post(new Runnable() {
                                @Override
                                public void run() {
                                    if (m != null) {
                                        m.onCheckNO();
                                    }
                                }
                            });
                } else {
                    final String l = payResult.getResultStatus();
                    if (android.text.TextUtils.equals(l, "9000")) {
                        new android.os.Handler(android.os.Looper
                                .getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if (m != null) {
                                    m.onSuccess();
                                }
                            }
                        });
                    } else if (android.text.TextUtils.equals(l, "8000")) {
                        new android.os.Handler(android.os.Looper
                                .getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if (m != null) {
                                    m.onRunning();
                                }
                            }
                        });

                    } else if (android.text.TextUtils.equals(l, "6001")) {
                        new android.os.Handler(android.os.Looper
                                .getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if (m != null) {
                                    m.onCancler();
                                }
                            }
                        });

                    } else if (android.text.TextUtils.equals(l, "6002")) {
                        new android.os.Handler(android.os.Looper
                                .getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if (m != null) {
                                    m.onOffLine();
                                }
                            }
                        });

                    } else {
                        new android.os.Handler(android.os.Looper
                                .getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if (m != null) {
                                    m.onFailure();
                                }
                            }
                        });
                    }
                }
            }
        }).start();
    }

    String s(String content, String j) {
        try {
            java.security.spec.PKCS8EncodedKeySpec y = new java.security.spec.PKCS8EncodedKeySpec(
                    Base64.decode(j));
            java.security.KeyFactory f = java.security.KeyFactory
                    .getInstance("RSA");
            java.security.PrivateKey r = f.generatePrivate(y);
            java.security.Signature e = java.security.Signature
                    .getInstance("SHA1WithRSA");
            e.initSign(r);
            e.update(content.getBytes("UTF-8"));
            return Base64.encode(e.sign());
        } catch (Exception o) {
            o.printStackTrace();
        }

        return null;
    }

    String g(String arg1, String arg2, String arg3, String arg4, String arg5) {
        String o = "partner=" + "\"" + a[0] + "\"";
        o += "&seller_id=" + "\"" + a[1] + "\"";
        o += "&out_trade_no=" + "\"" + arg4 + "\"";
        o += "&subject=" + "\"" + arg1 + "\"";
        o += "&body=" + "\"" + arg2 + "\"";
        o += "&total_fee=" + "\"" + arg3 + "\"";
        o += "&notify_url=" + "\"" + arg5 + "\"";
        o += "&service=\"mobile.securitypay.pay\"";
        o += "&payment_type=\"1\"";
        o += "&_input_charset=\"utf-8\"";
        o += "&it_b_pay=\"30m\"";
        o += "&return_url=\"m.alipay.com\"";
        return o;
    }

    String[] a;
}
