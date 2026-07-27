<img width="150" height="150" alt="KE_logo_cool" src="https://github.com/user-attachments/assets/68891974-f968-451f-a62a-83a1206e5ef7" />
<svg viewBox="0 0 1080 1080" xmlns="http://www.w3.org/2000/svg">
  <defs>
    <linearGradient id="bg" x1="0%" y1="0%" x2="100%" y2="100%">
      <stop offset="0%" stop-color="#04141c"/>
      <stop offset="55%" stop-color="#0a2530"/>
      <stop offset="100%" stop-color="#04141c"/>
    </linearGradient>

    <linearGradient id="letterGrad" x1="0%" y1="0%" x2="100%" y2="100%">
      <stop offset="0%" stop-color="#5cf7d8"/>
      <stop offset="45%" stop-color="#12d3ac"/>
      <stop offset="100%" stop-color="#00897b"/>
    </linearGradient>

    <linearGradient id="edgeGrad" x1="0%" y1="0%" x2="100%" y2="100%">
      <stop offset="0%" stop-color="#baffef"/>
      <stop offset="100%" stop-color="#12d3ac"/>
    </linearGradient>

    <radialGradient id="glow" cx="50%" cy="45%" r="65%">
      <stop offset="0%" stop-color="#12d3ac" stop-opacity="0.35"/>
      <stop offset="100%" stop-color="#12d3ac" stop-opacity="0"/>
    </radialGradient>

    <filter id="softGlow" x="-50%" y="-50%" width="200%" height="200%">
      <feGaussianBlur stdDeviation="14" result="blur"/>
      <feMerge>
        <feMergeNode in="blur"/>
        <feMergeNode in="SourceGraphic"/>
      </feMerge>
    </filter>

    <filter id="bigBlur" x="-50%" y="-50%" width="200%" height="200%">
      <feGaussianBlur stdDeviation="60"/>
    </filter>
  </defs>

  <!-- background -->
  <rect width="1080" height="1080" fill="url(#bg)"/>

  <!-- ambient glow behind letters -->
  <circle cx="540" cy="500" r="480" fill="url(#glow)"/>

  <!-- diagonal speed lines echoing the K -->
  <g stroke="#12d3ac" stroke-opacity="0.18" stroke-width="6">
    <line x1="120" y1="230" x2="40" y2="150"/>
    <line x1="160" y1="290" x2="60" y2="230"/>
    <line x1="200" y1="350" x2="90" y2="310"/>
  </g>

  <!-- thin accent frame -->
  <rect x="36" y="36" width="1008" height="1008" fill="none" stroke="#12d3ac" stroke-opacity="0.25" stroke-width="2"/>

  <!-- letterforms, slanted for energy, with glow -->
  <g transform="skewX(-8)" filter="url(#softGlow)">

    <!-- K -->
    <g fill="url(#letterGrad)">
      <rect x="150" y="230" width="140" height="600" />
      <polygon points="430,230 590,230 320,530 590,830 430,830 290,540" />
    </g>

    <!-- E -->
    <g fill="url(#letterGrad)">
      <rect x="640" y="230" width="410" height="140"/>
      <rect x="640" y="460" width="360" height="140"/>
      <rect x="640" y="690" width="410" height="140"/>
      <rect x="640" y="230" width="140" height="600"/>
    </g>

    <!-- crisp bright edge highlight on the K's diagonal -->
    <polygon points="430,230 590,230 470,370 400,290" fill="url(#edgeGrad)" opacity="0.85"/>
  </g>

  <!-- subtle bottom reflection -->
  <g transform="translate(0,1080) scale(1,-1)" opacity="0.08">
    <g transform="skewX(-8)" clip-path="inset(0px 0px 780px 0px)">
      <g fill="url(#letterGrad)">
        <rect x="150" y="230" width="140" height="300"/>
        <polygon points="430,230 590,230 320,530 400,610 290,540" />
        <rect x="640" y="230" width="410" height="140"/>
        <rect x="640" y="230" width="140" height="300"/>
      </g>
    </g>
  </g>
</svg>
