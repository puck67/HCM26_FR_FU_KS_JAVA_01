import { getAllTasks } from "@/lib/data";
import { NextResponse } from "next/server";

export async function GET() {
  const tasks = getAllTasks();
  return NextResponse.json(tasks);
}
