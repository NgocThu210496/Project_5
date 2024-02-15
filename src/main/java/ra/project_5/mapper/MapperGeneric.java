package ra.project_5.mapper;

public interface MapperGeneric <E,Q,S>{
    //E: entity
    //Q: Request
    //S: Response
    E mapperRequestToEntity(Q q);
    S mapperEntityToResponse(E e);
}
