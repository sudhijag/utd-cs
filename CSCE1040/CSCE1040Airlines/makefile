main: main.o Flight.o Flights.o Plane.o Planes.o Member.o Members.o
	g++ -o main main.o Flight.o Flights.o Plane.o Planes.o Member.o Members.o
Flight.o: Flight.cpp Flight.h
	g++ -c Flight.cpp
Planes.o: Planes.cpp Planes.h
	g++ -c Planes.cpp
Plane.o: Plane.cpp Plane.h
	g++ -c Plane.cpp
Member.o: Member.h Member.cpp
	g++ -c Member.cpp
main.o: main.cpp Members.h Flights.h Planes.h
	g++ -c  main.cpp
Members.o: Members.cpp Members.h
	g++ -c Members.cpp
Flights.o: Flights.cpp Flights.h
	g++ -c Flights.cpp
clean:
	rm main *.o
run:
	./main
