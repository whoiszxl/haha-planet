<template>
  <div class="login-bg">
    <!-- 微妙的网格背景 -->
    <div class="grid-overlay"></div>

    <!-- 星空效果 -->
    <div class="stars">
      <div v-for="n in 3" :key="`stars-${n}`" :class="`star star-${n}`"></div>
    </div>

    <!-- 自然光晕效果 -->
    <div class="ambient-glow">
      <div class="glow glow-1"></div>
      <div class="glow glow-2"></div>
      <div class="glow glow-3"></div>
    </div>

    <!-- 微妙粒子效果 -->
    <div class="particles">
      <div v-for="n in 15" :key="`particle-${n}`" class="particle"></div>
    </div>
  </div>
</template>

<script lang="ts" setup></script>

<style lang="scss" scoped>
@use "sass:math";
.login-bg {
  width: 100%;
  height: 100%;
  position: fixed;
  overflow: hidden;
  z-index: 1;
  background: linear-gradient(135deg, #0a0a0a 0%, #111827 20%, #1f2937 40%, #111827 60%, #0f172a 80%, #020617 100%);

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: radial-gradient(ellipse at 25% 75%, rgba(30, 58, 138, 0.05) 0%, transparent 70%),
                radial-gradient(ellipse at 75% 25%, rgba(67, 56, 202, 0.03) 0%, transparent 60%),
                radial-gradient(ellipse at 50% 50%, rgba(15, 23, 42, 0.8) 0%, transparent 50%);
    animation: subtleAurora 20s ease-in-out infinite alternate;
  }
}

// 网格背景
.grid-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image:
    linear-gradient(rgba(71, 85, 105, 0.03) 1px, transparent 1px),
    linear-gradient(90deg, rgba(71, 85, 105, 0.03) 1px, transparent 1px);
  background-size: 80px 80px;
  animation: gridMove 30s linear infinite;
  opacity: 0.6;
}

@keyframes gridMove {
  0% {
    transform: translate(0, 0);
  }
  100% {
    transform: translate(50px, 50px);
  }
}

@keyframes subtleAurora {
  0% {
    opacity: 0.3;
    transform: scale(1) rotate(0deg);
  }
  100% {
    opacity: 0.5;
    transform: scale(1.05) rotate(0.5deg);
  }
}

.stars {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.star {
  position: absolute;
  border-radius: 50%;
  background-color: rgba(203, 213, 225, 0.4);
  animation: twinkle 4s infinite ease-in-out;
  box-shadow: 0 0 4px rgba(148, 163, 184, 0.3);
}

$stars-small: 50;
$stars-medium: 20;
$stars-large: 10;

.star-1 {
  @for $i from 1 through $stars-small {
    &:nth-child(#{$i}) {
      top: #{random(100)}vh;
      left: #{random(100)}vw;
      width: 1px;
      height: 1px;
      animation-delay: #{math.div(random(20), 10)}s;
    }
  }
}

.star-2 {
  @for $i from 1 through $stars-medium {
    &:nth-child(#{$i}) {
      top: #{random(100)}vh;
      left: #{random(100)}vw;
      width: 2px;
      height: 2px;
      animation-delay: #{math.div(random(20), 10)}s;
    }
  }
}

.star-3 {
  @for $i from 1 through $stars-large {
    &:nth-child(#{$i}) {
      top: #{random(100)}vh;
      left: #{random(100)}vw;
      width: 3px;
      height: 3px;
      animation-delay: #{math.div(random(20), 10)}s;
    }
  }
}

// 自然光晕效果
.ambient-glow {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.glow {
  position: absolute;
  border-radius: 50%;
  filter: blur(40px);
  animation: gentleFloat 15s ease-in-out infinite;
}

.glow-1 {
  width: 300px;
  height: 300px;
  background: radial-gradient(circle, rgba(30, 58, 138, 0.1) 0%, transparent 70%);
  top: 10%;
  left: 20%;
  animation-delay: 0s;
}

.glow-2 {
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, rgba(67, 56, 202, 0.08) 0%, transparent 70%);
  bottom: 20%;
  right: 15%;
  animation-delay: 5s;
}

.glow-3 {
  width: 250px;
  height: 250px;
  background: radial-gradient(circle, rgba(15, 23, 42, 0.15) 0%, transparent 70%);
  top: 60%;
  left: 60%;
  animation-delay: 10s;
}

@keyframes gentleFloat {
  0%, 100% {
    transform: translate(0, 0) scale(1);
    opacity: 0.3;
  }
  33% {
    transform: translate(20px, -15px) scale(1.1);
    opacity: 0.5;
  }
  66% {
    transform: translate(-15px, 10px) scale(0.9);
    opacity: 0.4;
  }
}

// 粒子效果
.particles {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.particle {
  position: absolute;
  width: 1px;
  height: 1px;
  background: rgba(148, 163, 184, 0.4);
  border-radius: 50%;
  animation: particleFloat 12s linear infinite;
}

@for $i from 1 through 15 {
  .particle:nth-child(#{$i}) {
    left: #{random(100)}%;
    animation-delay: #{random(120) * 0.1}s;
    animation-duration: #{8 + random(80) * 0.1}s;
  }
}

@keyframes particleFloat {
  0% {
    transform: translateY(100vh) scale(0);
    opacity: 0;
  }
  10% {
    opacity: 1;
  }
  90% {
    opacity: 1;
  }
  100% {
    transform: translateY(-10vh) scale(1);
    opacity: 0;
  }
}

@keyframes twinkle {
  0% {
    opacity: 0;
    transform: scale(0.8);
  }
  50% {
    opacity: 1;
    transform: scale(1.2);
  }
  100% {
    opacity: 0;
    transform: scale(0.8);
  }
}
</style>
