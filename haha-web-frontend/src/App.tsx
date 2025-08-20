import React, { useEffect } from "react";
import styles from "./App.module.css";
import { BrowserRouter, Routes, Route, useLocation } from "react-router-dom";
import {
  HomePage,
  RegisterPage,
  LoginPage,
  OAuthRedirectPage,
  BindPage
} from "./pages";
import { SmsLoginPage } from "./pages/login/SmsLoginPage";
import { EmailLoginPage } from "./pages/login/EmailLoginPage";
import { NotFoundPage } from "./pages/common/NotFoundPage";
import { DiscoveryPage } from "./pages/discovery/DiscoveryPage";
import { PlanetDetailPage } from "./pages/planet-preview/PlanetPreviewPage";
import { PlanetContentPage } from "./pages/content";
import { PostDetailPage } from "./pages/post-detail";

// 滚动到顶部组件
const ScrollToTop = () => {
  const { pathname } = useLocation();

  useEffect(() => {
    window.scrollTo(0, 0);
  }, [pathname]);

  return null;
};

function App() {
  return (
    <div className={styles.App}>
      {/* 浏览器路由页面配置 */}
      <BrowserRouter>
        <ScrollToTop />
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/social/callback" element={<OAuthRedirectPage />} />
          <Route path="/social/bind" element={<BindPage />} />
          <Route path="/sms-login" element={<SmsLoginPage />} />
          <Route path="/email-login" element={<EmailLoginPage />} />

          <Route path="/discovery" element={<DiscoveryPage />} />
          <Route path="/preview/:planetId" element={<PlanetDetailPage />} />
          <Route path="/content" element={<PlanetContentPage />} />
          <Route path="/post/:postId" element={<PostDetailPage />} />

          <Route path="*" element={<NotFoundPage />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
