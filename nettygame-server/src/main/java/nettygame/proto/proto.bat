C:
cd C:\Users\Administrator\Desktop\netty-game-master\nettygame-server\src\main\java\nettygame\proto



set OUT=../generate
set def_room_java=(room)

for %%A in %def_room_java% do (
    echo generate room java code: %%A.proto
    protoc.exe --java_out=%OUT% ./room_def/%%A.proto
)

pause