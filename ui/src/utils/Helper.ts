export function formatToGermanDate(dateString: string): string {
  if (!dateString || isNaN(Date.parse(dateString))) {
    return "";
  }
  const date = new Date(dateString);
  const options: Intl.DateTimeFormatOptions = {
    year: "2-digit",
    month: "2-digit",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
    hour12: false,
  };
  return new Intl.DateTimeFormat("de-DE", options).format(date);
}
