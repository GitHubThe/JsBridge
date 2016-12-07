1) 使用com.github.lzyzsd.jsbridge.BridgeWebView替代WebView, 例如XML中:

```
<com.github.lzyzsd.jsbridge.BridgeWebView
    android:id="@+id/webView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
```

2) 在Java中注册handler, 调用JS的handler

```
webView = (BridgeWebView) findViewById(R.id.webView);

webView.registerHandler("testObjcCallback", new BridgeHandler() {
   @Override
   public void handler(String data, CallBackFunction function) {
       Log.i(TAG, "testObjcCallback called: " + data);
       function.onCallBack("Response from testObjcCallback");
   }
});

webView.callHandler("testJavascriptHandler", "{\"greetingFromObjC\": \"Hi there, JS!\"}", new CallBackFunction() {
   @Override
   public void onCallBack(String data) {
       Log.i(TAG, "testJavascriptHandler responded: " + data);
   }
});
```

3) 拷贝 setupWebViewJavascriptBridge 这段方法到JS中:

```
function setupWebViewJavascriptBridge(callback) {
    if (window.WebViewJavascriptBridge) { return callback(WebViewJavascriptBridge); }
    if (window.WVJBCallbacks) { return window.WVJBCallbacks.push(callback); }
    window.WVJBCallbacks = [callback];
    var WVJBIframe = document.createElement('iframe');
    WVJBIframe.style.display = 'none';
    WVJBIframe.src = 'wvjbscheme://__BRIDGE_LOADED__';
    document.documentElement.appendChild(WVJBIframe);
    setTimeout(function() { document.documentElement.removeChild(WVJBIframe) }, 0)
}
```

4) 最后, JS中执行 setupWebViewJavascriptBridge 方法, 并用bridge去注册handler和调用Java的handler

```
setupWebViewJavascriptBridge(function(bridge) {

    /* Initialize your app here */

    bridge.registerHandler('testJavascriptHandler', function(data, responseCallback) {
        console.log('ObjC called testJavascriptHandler with', data)
        var responseData = { 'Javascript Says':'Right back atcha!' }
        console.log('JS responding with', responseData)
        responseCallback(responseData)
    })
    bridge.callHandler('testObjcCallback', {'foo': 'bar'}, function(response) {
        console.log('JS got response', response)
    })
})
```

## License

This project is licensed under the terms of the MIT license.
