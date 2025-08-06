import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import App from "./App";
import "antd/dist/antd.min.css";
import axios from "axios";

axios.defaults.headers['api-key'] = '123456';

const root = ReactDOM.createRoot(
  document.getElementById("root") as HTMLElement
);
root.render(

  // 在开发模式下，React 的 Strict Mode 会导致某些生命周期方法和钩子被调用两次，以帮助开发者发现潜在的副作用。
  //如果你在使用 useEffect 等钩子进行 API 请求，这可能会导致请求被发送两次。
  // <React.StrictMode>
  //   <Provider store={rootStore.store}>
  //     <PersistGate persistor={rootStore.persistor}>
  //       <App />
  //     </PersistGate>
  //   </Provider>
  // </React.StrictMode>

  <App />
);
