\version "2.12.3"

#(define (pianoDynamics dynamic)
  (cond
    ((string=? dynamic "pp") 0.2)
    ((string=? dynamic "p") 0.4)
    ((string=? dynamic "f") 0.8)
    ((string=? dynamic "ff") 1.0)
    (else (default-dynamic-absolute-volume dynamic))))

global= {
  \time 4/4
  \tempo 4 = 160
  \key c \major
}

piano = \new Voice \relative c' {
  \clef treble
  \set midiInstrument = "Pizzicato Strings"
  \set Score.dynamicAbsoluteVolumeFunction = #pianoDynamics
  
  a8\f b8 c8 a8 b8 c8 a8 b8
  c8\p b8 a8 c8 b8 a8 c8 b8
  a8\f b8 c8 a8 b8 c8 a8 b8
  c8\p b8 a8 c8 b8 a8 c8 b8
  b8\f c8 d8 b8 c8 d8 b8 c8
  d8\p c8 b8 d8 c8 b8 d8 b8
  a8\f b8 d8 a8 b8 d8 a8 b8
  d8\p b8 a8 d8 b8 a8 d8 b8
  
  a8\f b8 c8 a8 b8 c8 a8 b8
  c8\p b8 a8 c8 b8 a8 c8 b8
  a8\f b8 c8 a8 b8 c8 a8 b8
  c8\p b8 a8 c8 b8 a8 c8 b8
  b8\f c8 d8 b8 c8 d8 b8 c8
  d8\p c8 b8 d8 c8 b8 d8 b8
  a8\f b8 d8 a8 b8 d8 a8 b8
  d8\p b8 a8 d8 b8 a8 d8 b8
  
  a8\f b8 c8 a8 b8 c8 a8 b8
  c8\p b8 a8 c8 b8 a8 c8 b8
  a8\f b8 c8 a8 b8 c8 a8 b8
  c8\p b8 a8 c8 b8 a8 c8 b8
  b8\f c8 d8 b8 c8 d8 b8 c8
  d8\p c8 b8 d8 c8 b8 d8 b8
  a8\f b8 d8 a8 b8 d8 a8 b8
  d8\p b8 a8 d8 b8 a8 d8 b8
}

violin = \new Voice \relative a' {
  \clef treble
  \set midiInstrument = "Violin"
  
  \times 8/1 { r1 }
  
  a1~a1
  f1~f1
  g1~g1
  d1~d1
  
  a'1~a1
  f1~f1
  <g b,>1~<g b>1
  <d f a>1~<d f a>1
}

\score {
  \new StaffGroup <<
    \new Staff << \global \piano >>
    \new Staff << \global \violin >>
  >>
  \layout {}
  \midi {
    \context {
      \Staff
      \remove "Staff_performer"
    }
    \context {
      \Voice
      \consists "Staff_performer"
    }
    \context {
      \Score
      tempoWholesPerMinute = #(ly:make-moment 72 2)
    }
  }
}
