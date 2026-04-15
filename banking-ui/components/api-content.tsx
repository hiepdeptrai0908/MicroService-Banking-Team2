"use client";
import { Info } from "lucide-react";
import { Badge } from "@/components/ui/badge";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Separator } from "@/components/ui/separator";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { apiDocs } from "@/lib/api-docs";
import { methodColor, type NavItem } from "@/lib/nav";

export function ApiContent({
  item,
  children,
}: {
  item: NavItem;
  children: React.ReactNode;
}) {
  const doc = apiDocs[item.id];
  return (
    <Card>
      <CardHeader>
        <div className="flex items-center gap-3">
          <item.icon className="h-5 w-5 text-primary" />
          <div className="flex-1">
            <CardTitle className="flex items-center gap-2">
              {item.label}
              <Badge
                variant="outline"
                className={`font-mono text-xs ${methodColor[item.method]}`}
              >
                {item.method}
              </Badge>
            </CardTitle>
            <CardDescription className="mt-1">
              {doc?.desc || item.desc}
            </CardDescription>
          </div>
        </div>
        <Separator className="mt-3" />
      </CardHeader>
      <CardContent>
        <Tabs defaultValue="test">
          <TabsList className="mb-4">
            <TabsTrigger value="test">Test API</TabsTrigger>
            <TabsTrigger value="flow">
              <Info className="h-3.5 w-3.5 mr-1.5" />
              Luồng hoạt động
            </TabsTrigger>
          </TabsList>
          <TabsContent value="test" className="space-y-4">
            {children}
          </TabsContent>
          <TabsContent value="flow" className="space-y-3">
            {doc && (
              <>
                <ol className="space-y-2">
                  {doc.steps.map((step, i) => (
                    <li
                      key={i}
                      className="flex gap-3 text-sm text-muted-foreground"
                    >
                      <span className="shrink-0 w-6 h-6 rounded-full bg-primary/10 text-primary text-xs font-bold flex items-center justify-center">
                        {i + 1}
                      </span>
                      <span className="pt-0.5">{step}</span>
                    </li>
                  ))}
                </ol>
                {doc.notes && (
                  <div className="flex gap-2 p-3 rounded-lg bg-primary/5 border border-primary/20 mt-3">
                    <Info className="h-4 w-4 text-primary mt-0.5 shrink-0" />
                    <p className="text-xs text-muted-foreground">{doc.notes}</p>
                  </div>
                )}
              </>
            )}
          </TabsContent>
        </Tabs>
      </CardContent>
    </Card>
  );
}
