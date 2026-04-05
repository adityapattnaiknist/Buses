const nSeats = 10000;
const mSelectedSeats = 1000;

const trip = { seats: Array.from({ length: nSeats }, (_, i) => ({ id: i, isBooked: false })) };
const selectedSeats = Array.from({ length: mSelectedSeats }, (_, i) => ({ id: i * 2 }));

console.log("Baseline:");
console.time("baseline");
for (let j = 0; j < 100; j++) {
    trip.seats.map(seat => {
        return selectedSeats.find(s => s.id === seat.id) ? true : false;
    });
}
console.timeEnd("baseline");

console.log("\nOptimized:");
console.time("optimized");
for (let j = 0; j < 100; j++) {
    const selectedSeatIds = new Set(selectedSeats.map(s => s.id));
    trip.seats.map(seat => {
        return selectedSeatIds.has(seat.id) ? true : false;
    });
}
console.timeEnd("optimized");
