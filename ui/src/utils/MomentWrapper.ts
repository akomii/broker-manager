import moment, { Duration } from "moment";

export class MomentWrapper {
    static formatDateToGermanLocale(date: Date | undefined | null): string {
        return date ? moment(date).locale("de").format("DD.MM.YYYY HH:mm") : "";
    }

    static createDuration(value: any): Duration {
        return moment.duration(value);
    }

    static computePeriod(date1: Date, date2: Date): Duration {
        const roundedDate1 = moment(date1).startOf("minute");
        const roundedDate2 = moment(date2).startOf("minute");
        return moment.duration(roundedDate1.diff(roundedDate2));
    }

    static addDurationToDate(referenceDate: Date, duration: Duration): Date {
        return moment(referenceDate).add(duration).toDate();
    }
}

export type MomentDuration = Duration;

export default MomentWrapper;
