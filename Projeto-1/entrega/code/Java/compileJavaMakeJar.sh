javac \
./OTP-Break/src/config/Config.java \
./OTP-Break/src/feature/crib/CribDrag.java \
./OTP-Break/src/feature/cryptoanalysis/CryptoAnalysis.java \
./OTP-Break/src/feature/cryptoanalysis/CryptoAnalysisBase.java \
./OTP-Break/src/feature/cryptoanalysis/CryptoAnalysisEnd.java \
./OTP-Break/src/feature/cryptoanalysis/CryptoAnalysisMiddle.java \
./OTP-Break/src/feature/cryptoanalysis/CryptoAnalysisStart.java \
./OTP-Break/src/feature/cryptoanalysis/semantic/SemanticAnalysis.java \
./OTP-Break/src/model/CipherText.java \
./OTP-Break/src/model/Crib.java \
./OTP-Break/src/model/Dictionary.java \
./OTP-Break/src/model/Key.java \
./OTP-Break/src/model/PartialPlainTexts.java \
./OTP-Break/src/model/PlainTexts.java \
./OTP-Break/src/otpbreak/Main.java \
./OTP-Break/src/thread/ThreadMonitor.java \
./OTP-Break/src/thread/crib/CribDragThread.java \
./OTP-Break/src/thread/cryptoanalysis/CryptoAnalysisEndThread.java \
./OTP-Break/src/thread/cryptoanalysis/CryptoAnalysisMiddleThread.java \
./OTP-Break/src/thread/cryptoanalysis/CryptoAnalysisStartThread.java \
./OTP-Break/src/util/Chronometer.java \
./OTP-Break/src/util/CipherUtils.java \
./OTP-Break/src/util/FileUtils.java \
./OTP-Break/src/util/StringUtils.java
jar cvfm OTP-Break.jar ./OTP-Break/src/META-INF/MANIFEST.MF -C OTP-Break/src/ .
