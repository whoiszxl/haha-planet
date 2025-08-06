import React from "react";
import styles from "./HomePage.module.css";
import { Footer, Header } from "../../components";

export const HomePage: React.FC = () => {
  return (
    <div className={styles.homePage}>
      <Header/>
      <h1>Hello World</h1>
      <Footer/>
    </div>
  );
};