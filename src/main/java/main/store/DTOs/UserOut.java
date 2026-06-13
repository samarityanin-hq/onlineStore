package DTOs;

import Repositories.UserRepo;

public record UserOut(String name,
                      String email){}
