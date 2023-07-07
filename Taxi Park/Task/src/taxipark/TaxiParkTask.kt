package taxipark

import java.util.stream.Collectors
import kotlin.math.floor
import kotlin.math.roundToInt

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> {
    val driversWithoutTrips = HashSet(this.allDrivers);

    val executedDriverList = arrayListOf<String>();

    this.trips.stream()
        .filter{!executedDriverList.contains(it.driver.name)}
        .map{ driversWithoutTrips.remove(it.driver) }
        .collect(Collectors.toSet());

    return driversWithoutTrips;
}


/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> {

    if (minTrips == 0) return this.allPassengers;

    val clientsTripsMap = mutableMapOf<Passenger, Int>();
    val correctClientsSet = hashSetOf<Passenger>();

    this.trips.flatMap { trip -> trip.passengers }
        .forEach{pass ->
            clientsTripsMap[pass] = clientsTripsMap[pass]?.inc() ?: 1;
        }


    clientsTripsMap
        .filter{ entry -> entry.value >= minTrips }
        .forEach{entry -> correctClientsSet.add(entry.key) };

    return correctClientsSet;
}

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> {

    val clientsOneTrip = hashSetOf<Passenger>();
    val correctClientsSet = hashSetOf<Passenger>();


    this.trips.stream()
        .filter { trip -> trip.driver == driver }
        .flatMap { trip -> trip.passengers.stream() }
        .forEach { pass ->
            if (correctClientsSet.contains(pass)) {
                return@forEach
            }

            if (clientsOneTrip.contains(pass)) {
                correctClientsSet.add(pass);
            } else {
                clientsOneTrip.add(pass);
            }
        }

    return correctClientsSet;
}

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> {
    val discountPassMap: HashMap<Passenger, Int> = hashMapOf();
    val withoutDiscPassMap: HashMap<Passenger, Int> = hashMapOf();

    this.trips.stream()
        .forEach { trip ->
            trip.passengers.forEach { passenger ->
                if (trip.discount != null) {
                    discountPassMap[passenger] =
                        discountPassMap[passenger]?.inc() ?: 1;
                } else {
                    withoutDiscPassMap[passenger] =
                        withoutDiscPassMap[passenger]?.inc() ?: 1;
                }
            }
        }

    val correctClientsSet = hashSetOf<Passenger>();

    discountPassMap.map{ (pass, count) ->
        val withoutDiscCount = withoutDiscPassMap[pass];

        if (withoutDiscCount == null || count > withoutDiscCount) {
            correctClientsSet.add(pass);
        }
    }

    return correctClientsSet;
}

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    fun getPeriod(num: Int): IntRange {
        val start: Int = (floor(num/10.0) * 10).roundToInt();
        val end: Int = (((num + 5) / 10.0).roundToInt() * 10) - 1;

        return IntRange(start, end);
    }


    val tripsDurationsMap: HashMap<IntRange, Int> = hashMapOf();

    this.trips.forEach { trip ->
        val tripPeriod: IntRange = getPeriod(trip.duration);

        tripsDurationsMap[tripPeriod] = tripsDurationsMap[tripPeriod]?.inc() ?: 1;
    }


    val mostFrequentPeriod: IntRange? =
        tripsDurationsMap.maxByOrNull{ it.value }?.key;


    return mostFrequentPeriod
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    if (this.trips.isEmpty()) return false

    val driverIncomeMap: HashMap<Driver, Double> = hashMapOf();

    var totalIncome: Double = 0.0;

    this.trips.forEach { trip ->
        driverIncomeMap[trip.driver] = driverIncomeMap[trip.driver]?.plus(trip.cost) ?: trip.cost;
        totalIncome = totalIncome.plus(trip.cost);
    }

    var mustExecDriversIncome: Double = 0.0;
    val mustExecDriversIndexFrom: Int = driverIncomeMap.count() - this.allDrivers.count() / 5;

    driverIncomeMap.toList()
        .sortedBy { (_, value) -> value }
        .mapIndexed { index, pair ->
            if (index < mustExecDriversIndexFrom) return@mapIndexed

            mustExecDriversIncome = mustExecDriversIncome.plus(pair.second);
        }

    return totalIncome * 0.8 <= mustExecDriversIncome;
}
