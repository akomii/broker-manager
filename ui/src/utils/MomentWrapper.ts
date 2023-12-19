import moment, { Duration } from "moment";

export class MomentWrapper {
    static formatDateToGermanLocale(date: Date | undefined | null): string {
        return date ? moment(date).locale("de").format("DD.MM.YYYY HH:mm") : "";
    }

    static createDuration(value: any): Duration {
        return moment.duration(value);
    }
}

export type MomentDuration = Duration;

export default MomentWrapper;
