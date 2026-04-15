export type ApiRes = { status: number; data: unknown } | null;

export async function api(url: string, opts?: RequestInit): Promise<ApiRes> {
  try {
    const r = await fetch(url, opts);
    const text = await r.text();
    let data;
    try { data = JSON.parse(text); } catch { data = text; }
    return { status: r.status, data };
  } catch (e) {
    return { status: 0, data: String(e) };
  }
}

export function authHeader(token: string): HeadersInit {
  return { Authorization: `Bearer ${token}`, "Content-Type": "application/json" };
}
