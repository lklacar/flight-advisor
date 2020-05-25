package com.htecgroup.flightadvisor.service.impl;

import com.htecgroup.flightadvisor.domain.City;
import com.htecgroup.flightadvisor.domain.projection.CityCommentProjection;
import com.htecgroup.flightadvisor.exception.UniqueConstraintViolatedException;
import com.htecgroup.flightadvisor.repository.CityRepository;
import com.htecgroup.flightadvisor.service.CityService;
import com.htecgroup.flightadvisor.service.dto.CityDto;
import com.htecgroup.flightadvisor.service.mapper.CityCommentProjectionMapper;
import com.htecgroup.flightadvisor.service.mapper.CityMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;
    private final CityMapper cityMapper;
    private final CityCommentProjectionMapper cityCommentProjectionMapper;

    public CityServiceImpl(
            CityRepository cityRepository,
            CityMapper cityMapper,
            CityCommentProjectionMapper cityCommentProjectionMapper
    ) {
        this.cityRepository = cityRepository;
        this.cityMapper = cityMapper;
        this.cityCommentProjectionMapper = cityCommentProjectionMapper;
    }

    @Override
    public CityDto createCity(CityDto cityDto) {
        cityRepository.findByNameAndCountry(cityDto.getName(), cityDto.getCountry())
                .ifPresent(city -> {
                    throw new UniqueConstraintViolatedException(String.format("City in \"%s\" with name \"%s\" already exists",
                            city.getCountry(),
                            cityDto.getName()
                    ));
                });
        City city = cityMapper.fromDto(cityDto);
        city = cityRepository.save(city);
        return cityMapper.toDto(city);
    }

    @Override
    public List<CityDto> listCities(Integer numberOfComments, String cityName) {
        var cityCommentProjections = cityRepository.getCitiesWithComments(numberOfComments, cityName);
        var cities = new ArrayList<CityDto>();

        cityCommentProjections.forEach(cityCommentProjection -> findCityByCityProjection(cities,
                cityCommentProjection
        ).ifPresentOrElse(addCityCommentProjectionToExistingCity(cityCommentProjection),
                createCityAndAddCityCommentProjection(cities, cityCommentProjection)
        ));
        return cities;
    }

    private Runnable createCityAndAddCityCommentProjection(
            ArrayList<CityDto> cities, CityCommentProjection cityCommentProjection
    ) {
        return () -> {
            var city = cityCommentProjectionMapper.toCity(cityCommentProjection);
            var comment = cityCommentProjectionMapper.toComment(cityCommentProjection);
            cities.add(city);
            city.setComments(new HashSet<>());
            Optional.ofNullable(comment.getId())
                    .ifPresent(commentId -> city.getComments()
                            .add(comment));
        };
    }

    private Consumer<CityDto> addCityCommentProjectionToExistingCity(CityCommentProjection cityCommentProjection) {
        return city -> city.getComments()
                .add(cityCommentProjectionMapper.toComment(cityCommentProjection));
    }

    private Optional<CityDto> findCityByCityProjection(
            ArrayList<CityDto> cities, CityCommentProjection cityCommentProjection
    ) {
        return cities.stream()
                .filter(city -> city.getId()
                        .equals(cityCommentProjection.getCityId()))
                .findFirst();
    }

}
