import { useBackend } from "@/services/backend";
import { paths } from "@/services/backend/endpoints";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";

export function useCounter() {
  const { backend } = useBackend();
  const queryClient = useQueryClient();
  const { data: count, isLoading } = useQuery({
    queryKey: ["/api/counter"],
    queryFn: () =>
      backend
        .get<
          paths["/api/counter"]["get"]["responses"]["200"]["content"]["application/json"]
        >("/api/counter")
        .then((_) => _.data),
  });
  const { mutate: addOne, isPending } = useMutation({
    mutationFn: () =>
      backend
        .post<
          paths["/api/counter/add-one"]["post"]["responses"]["200"]["content"]["application/json"]
        >("/api/counter/add-one")
        .then((_) => _.data),
    onSuccess: (data) => queryClient.setQueryData(["/api/counter"], data),
  });

  return {
    count,
    addOne: () => addOne(),
    isProgressing: isLoading || isPending,
  };
}
