import { getAllTodos } from "@/lib/data";
import { NextResponse } from "next/server";

export async function GET() {
    const todos = getAllTodos();
    return NextResponse.json(todos);
}
